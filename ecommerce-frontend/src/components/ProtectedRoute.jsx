// src/components/ProtectedRoute.jsx
import { Navigate, useLocation } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token");
  const location = useLocation();

  if (!token) return <Navigate to="/login" />;

  // Guard Admin Routes
  if (location.pathname.startsWith("/admin")) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.role !== "ADMIN") {
        return <Navigate to="/products" />;
      }
    } catch (e) {
      return <Navigate to="/login" />;
    }
  }

  return children;
};

export default ProtectedRoute;
