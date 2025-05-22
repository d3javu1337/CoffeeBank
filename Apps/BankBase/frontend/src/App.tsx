import React, {FC, useContext, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import LoginForm from "./component/LoginForm";
import {Context} from "./index";
import {observer} from "mobx-react-lite";
import {BrowserRouter} from "react-router-dom";
import AppRouter from "./component/AppRouter";

const App: FC = () => {

  const {store} = useContext(Context);
  useEffect(() => {
    if(localStorage.getItem('token')){
      store.checkAuth()
    }
  }, [])

    if(store.isLoading){
        return (
            <div>loading...</div>
        )
    }

  return (
      <BrowserRouter>
          <AppRouter/>
          {/*<div className="App">*/}
          {/*    <h3>{store.isAuth ? 'authed' :  'not authed'}</h3>*/}
          {/*    <LoginForm/>*/}
          {/*</div>*/}
      </BrowserRouter>
    // <div className="App">
    //   <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //     <p>
    //       Edit <code>src/App.tsx</code> and save to reload.
    //     </p>
    //     <a
    //       className="App-link"
    //       href="https://reactjs.org"
    //       target="_blank"
    //       rel="noopener noreferrer"
    //     >
    //       Learn React
    //     </a>
    //   </header>
    // </div>
  );
}

export default observer(App);
