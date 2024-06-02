import { useEffect, useState } from "react";

function useFetch({ baseUrl, method }) {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetch(baseUrl, {
      method: { method },
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((res) => res.json())
      .then((data) => setData(data))
      .catch((error) => alert(error));
  }, [baseUrl]);

  return data;
}

export default useFetch;
