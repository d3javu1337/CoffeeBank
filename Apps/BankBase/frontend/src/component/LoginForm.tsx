import React, {FC, useContext, useState} from 'react';
import {Context} from "../index";
import {observer} from "mobx-react-lite";

const LoginForm: FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const {store} = useContext(Context);
    return (
        <div>
            <input
                onChange={(e) => setEmail(e.target.value)}
                value={email} type="email" placeholder="email@email.ru" required />
            <input
                onChange={(e) => setPassword(e.target.value)}
                value={password} type="password" placeholder="********" required />
            <button onClick={() => store.login(email, password)}>login</button>
            </div>
    );
};

export default observer(LoginForm);