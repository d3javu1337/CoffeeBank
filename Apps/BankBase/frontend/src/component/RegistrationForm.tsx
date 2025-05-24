import React, {FC, useContext, useState} from 'react';
import {Context} from "../index";
import {observer} from "mobx-react-lite";

/*
        public readonly surname: string,
        public readonly name: string,
        public readonly patronymic: string,
        public readonly dateOfBirth: Date,
        public readonly phoneNumber: String,
        public readonly email: String,
        public readonly password: String
 */

const RegistrationForm: FC = () => {
    const [surname, setSurname] = useState('');
    const [name, setName] = useState('');
    const [patronymic, setPatronymic] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const {store} = useContext(Context);
    return (
        <div style={{ display: 'flex', flexDirection: 'column' , justifyContent: 'center', alignItems: 'center' }}>
            <input
                onChange={(e) => setSurname(e.target.value)}
                value={surname} type="text" placeholder="Ivanov" required />
            <input
                onChange={(e) => setName(e.target.value)}
                value={name} type="text" placeholder="Ivan" required />
            <input
                onChange={(e) => setPatronymic(e.target.value)}
                value={patronymic} type="text" placeholder="Ivanovich" required />
            <input
                onChange={(e) => setDateOfBirth(e.target.value)}
                value={dateOfBirth} type="date" required />
            <input
                onChange={(e) => setPhoneNumber(e.target.value)}
                value={phoneNumber} type="tel" placeholder="+78228133752" minLength={10} maxLength={10} required />
            <input
                onChange={(e) => setEmail(e.target.value)}
                value={email} type="email" placeholder="email@email.ru" required />
            <input
                onChange={(e) => setPassword(e.target.value)}
                value={password} type="password" placeholder="********" minLength={8} required />
            <button onClick={() =>
                store.registration(surname, name, patronymic, dateOfBirth, phoneNumber, email, password)}
                    type="submit">submit</button>
        </div>
    );
};

export default observer(RegistrationForm);