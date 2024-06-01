import { Route, Routes } from "react-router-dom";

import "./App.css";
import Home from "./Home/Home";
import LandingPageLayout from "./layout/LandingPageLayout";
import Login from "./login/Login";
import SignUp from "./signup/SignUp";
import Information from "./signup/Information";
import ResetPassword from "./forgot_password/ResetPassword";
import OtpScreen from "./forgot_password/OtpScreen";
import ConfirmPassword from "./forgot_password/ConfirmPassword";
import Collections from "./Home/Collections";
import Blogs from "./Home/Blogs";
import About from "./Home/About";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPageLayout />}>
        <Route index element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/info" element={<Information />} />
        <Route path="/reset_password" element={<ResetPassword />} />
        <Route path="/otp" element={<OtpScreen />} />
        <Route path="/new_password" element={<ConfirmPassword />} />
        <Route path="/collections_page" elements={<Collections />} />
        <Route path="/blogs_page" elements={<Blogs />} />
        <Route path="/about_page" elements={<About />} />
      </Route>
    </Routes>
  );
}

export default App;
