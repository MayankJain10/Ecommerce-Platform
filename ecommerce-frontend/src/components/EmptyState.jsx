import React from "react";
import Lottie from "lottie-react";
import emptyAnim from "../assets/empty.json";

const EmptyState = ({ message }) => {
  return (
    <div className="flex flex-col items-center justify-center py-10">
      <Lottie animationData={emptyAnim} loop className="w-72 h-72" />
      <p className="text-lg text-gray-700 mt-4">{message}</p>
    </div>
  );
};

export default EmptyState;
