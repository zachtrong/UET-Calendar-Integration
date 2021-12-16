import React from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router,Switch, Route,Link} from "react-router-dom";
import './css/App.css';
import SignUp from './components/SignUp';
import Login from './components/Login';
function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar navbar-expand-lg navbar-light fixed-top">
          <div className="container">
            <Link className="navbar-brand" to={"/"}>Διάβολος</Link>
              <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                <ul className="navbar-nav">
                    <Link style={{color: 'black', textDecoration:'none'}} to={"/sign-in"}>Sign in</Link> /
                    <Link style={{color: 'black', textDecoration:'none'}} to={"/sign-up"}>Sign up</Link>
                </ul>
              </div>
            </div>
        </nav>
            <div className="outer">
              <div className="inner">
                <Switch>
                  <Route style={{padding: '40px 55px 45px 55px'}} path="/sign-up" component={SignUp} />
                  <Route style={{padding: '40px 55px 45px 55px'}} path="/sign-in" component={Login} />
                </Switch>
              </div>
            </div>
      </div>
    </Router>         
  );
}
export default App;