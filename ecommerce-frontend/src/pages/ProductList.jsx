// src/pages/ProductList.jsx
import { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard";
import axios from "axios";
import Spinner from "../components/Spinner";
import Lottie from "lottie-react";
import emptyAnim from "../assets/empty.json"; // ✅ Add this animation file

const ProductList = () => {
  const [search, setSearch] = useState("");
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("http://localhost:8081/api/products/allproducts", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setProducts(res.data))
      .catch((err) => console.error("Error fetching product list", err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Spinner />;

  const filtered = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-4">
      <div className="mb-4 flex justify-center">
        <input
          type="text"
          placeholder="🔍 Search product..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full md:w-1/3 p-2 border rounded-lg shadow"
        />
      </div>

      {filtered.length === 0 ? (
        <div className="flex flex-col items-center justify-center mt-10">
          <Lottie animationData={emptyAnim} loop className="h-64 w-64" />
          <p className="text-lg text-gray-600 dark:text-gray-300 mt-4">
            No products found.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {filtered.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ProductList;
