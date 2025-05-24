import React, {FC, useContext, useEffect} from 'react';
import './App.css';
import {Context} from "./index";
import {observer} from "mobx-react-lite";
import {BrowserRouter} from "react-router-dom";
import AppRouter from "./component/AppRouter";
import Header from "./component/Header";

const App: FC = () => {

  const {store} = useContext(Context);
  useEffect(() => {
    if(localStorage.getItem('token')){
      store.checkAuth()
    }
  }, [store])

    if(store.isLoading){
        return (
            <div>loading...</div>
        )
    }

  return (
      <BrowserRouter>
          <Header />
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
