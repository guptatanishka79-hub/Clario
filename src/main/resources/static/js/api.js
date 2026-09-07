/**
 * Clario API client.
 * Thin wrapper around fetch() that always sends the session cookie and
 * normalizes error handling so pages can just `await api.get(...)`.
 */
const api = (() => {
  async function request(method, url, body) {
    const options = {
      method,
      credentials: 'include',
      headers: {},
    };
    if (body !== undefined) {
      options.headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(body);
    }

    let response;
    try {
      response = await fetch(url, options);
    } catch (networkErr) {
      throw new ApiError('Could not reach the server. Please check your connection and try again.', 0, null);
    }

    let data = null;
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      try {
        data = await response.json();
      } catch (e) {
        data = null;
      }
    }

    if (!response.ok) {
      const message = (data && data.message) ? data.message : `Request failed (${response.status})`;
      throw new ApiError(message, response.status, data);
    }

    return data;
  }

  return {
    get: (url) => request('GET', url),
    post: (url, body) => request('POST', url, body === undefined ? {} : body),
    put: (url, body) => request('PUT', url, body === undefined ? {} : body),
    del: (url) => request('DELETE', url),
  };
})();

class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.status = status;
    this.data = data;
    this.fieldErrors = data && data.fieldErrors ? data.fieldErrors : null;
  }
}
