import React, {FC, useState} from 'react';
import RegistrationForm from "../component/RegistrationForm";
import {observer} from "mobx-react-lite";
import LoginForm from "../component/LoginForm";

const AuthPage: FC = () => {
    const [isLoginPage, setIsLoginPage] = useState(true);

    if (!isLoginPage) {
        return (
            <div style={{width:'100%', border:'1px solid black',display:'flex', flexDirection:'column', justifyContent:'center', alignItems: 'center'}}>
                <RegistrationForm/>
                <button onClick={() => setIsLoginPage(true)}>switch to login</button>
            </div>
    );
    } else {
        return (
            <div style={{width:'100%', border:'1px solid black', display:'flex', flexDirection:'column', height:'100%', justifyContent:'center', alignItems: 'center'}}>
                <LoginForm/>
                <button onClick={() => setIsLoginPage(false)}>switch to registration</button>
            </div>
        );
    }
};

export default observer(AuthPage);