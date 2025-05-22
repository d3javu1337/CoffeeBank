import React, {FC, useContext} from 'react';
import {Routes, Route, Navigate} from 'react-router-dom';
import {Context} from "../index";
import {authenticatedRoutes, publicRoutes} from "../routes/routes";

const AppRouter: FC = () => {
    const {store} = useContext(Context);
    return (
        <Routes>
            {store.isAuth && authenticatedRoutes.map(({path, Component}) =>
                <Route key={path} path={path} Component={Component} />
            )}
            {publicRoutes.map(({path, Component}) =>
                <Route key={path} path={path} Component={Component} />
            )}
            <Route path="*" element={<Navigate to="/home" replace/>}/>
            </Routes>
    );
};

export default AppRouter;