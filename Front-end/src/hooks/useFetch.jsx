import { useEffect, useState } from "react";

function useFetch({ baseUrl, method, options = {} }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch(baseUrl, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      body: options.body ? JSON.stringify(options.body) : null,
      ...options,
    })
      .then((res) => res.json())
      .then((data) => setData(data))
      .catch((error) => setError(error));
  }, [baseUrl, method, options]);

  return { data, error };
}

export default useFetch;
