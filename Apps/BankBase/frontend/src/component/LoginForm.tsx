import React, {FC, useContext, useState} from 'react';
import {Context} from "../index";
import {observer} from "mobx-react-lite";
import {useNavigate} from "react-router-dom";

const LoginForm: FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const {store} = useContext(Context);
    const nav = useNavigate();
    return (
        <div style={{ display: 'flex', flexDirection: 'column' , justifyContent: 'center', width:'100%',alignItems: 'center' , height:'100%'}}>
            <input
                onChange={(e) => setEmail(e.target.value)}
                value={email} type="email" placeholder="email@email.ru" required />
            <input
                onChange={(e) => setPassword(e.target.value)}
                value={password} type="password" placeholder="********" required />
            <button onClick={() => {
                store.login(email, password).then(() => nav('/main'))
            }}>login</button>
            </div>
    );
};

export default observer(LoginForm);