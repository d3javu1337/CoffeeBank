import React, {FC, useContext} from 'react';
import {Context} from "../index";
import {NavLink, useNavigate} from "react-router-dom";

const Header: FC = () => {
    const {store} = useContext(Context);
    const nav = useNavigate();

    if (store.isAuth){
        return (
            <div style={{width:'100%', border:'1px solid black', display:'flex',flexDirection:'row', justifyContent:'space-between'}}>
                <NavLink to="/main">main page</NavLink>
                <button onClick={() => store.logout().then(() => nav('/home'))}>logout</button>
            </div>
        );
    } else{
        return (
            <div style={{width:'100%', border:'1px solid black'}}>
                <NavLink to="/auth">auth</NavLink>
            </div>
        );
    }
};

export default Header;