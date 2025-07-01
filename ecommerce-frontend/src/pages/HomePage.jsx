// src/pages/HomePage.jsx
import React from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import Lottie from "lottie-react";
import shoppingAnim from "../assets/home.json";

const HomePage = () => {
  const token = localStorage.getItem("token");

  return (
    <div className="min-h-screen bg-gradient-to-r from-blue-300 via-indigo-400 to-blue-700 flex flex-col items-center justify-center text-white px-4">
      <motion.div
        initial={{ opacity: 0, y: 40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="text-center"
      >
        <Lottie animationData={shoppingAnim} loop className="w-80 h-80 mx-auto mb-6" />
        <h1 className="text-5xl font-extrabold mb-4">Welcome to Your Shop!</h1>
        <p className="text-xl mb-8">Explore, shop and enjoy a seamless experience.</p>

        {!token && (
          <div className="flex gap-4 justify-center">
            <Link to="/login">
              <button className="bg-yellow-400 text-blue-900 px-6 py-2 rounded-lg font-bold hover:bg-yellow-300 transition">
                Login
              </button>
            </Link>
            <Link to="/register">
              <button className="bg-white text-blue-900 px-6 py-2 rounded-lg font-bold hover:bg-gray-200 transition">
                Register
              </button>
            </Link>
          </div>
        )}
      </motion.div>
    </div>
  );
};

export default HomePage;
