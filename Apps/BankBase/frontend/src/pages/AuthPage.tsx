import React, {FC, useState} from 'react';
import RegistrationForm from "../component/RegistrationForm";
import {observer} from "mobx-react-lite";
import LoginForm from "../component/LoginForm";

const AuthPage: FC = () => {
    const [isLoginPage, setIsLoginPage] = useState(true);

    if (!isLoginPage) {
        return (
            <div>
                <RegistrationForm/>
                <button onClick={() => setIsLoginPage(true)}>switch to login</button>
            </div>
    );
    } else {
        return (
            <div>
                <LoginForm/>
                <button onClick={() => setIsLoginPage(false)}>switch to registration</button>
            </div>
        );
    }
};

export default observer(AuthPage);