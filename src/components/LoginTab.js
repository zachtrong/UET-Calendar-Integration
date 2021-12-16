import React from "react";
import '../css/Login-Form-Clean.css';
import '../bootstrap/css/bootstrap.min.css';
import Alert from 'react-bootstrap/Alert'
import LoginButton from './google.login/LoginButton';
import LogoutButton from './google.login/LogoutButton';

class LoginTab extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            visible : false,
            isLogInUETCoursesSuccess : false
        }
    }

    onShowAlert = ()=>{
        this.setState({visible:true},()=>{
            window.setTimeout(()=>{
                this.setState({visible:false})
            },3000)
        });
    }

    submitLoginUetCourses = async() => {

        var serialize = require('form-serialize');
        var form = document.querySelector('#uet-auth-form');

        var uetCreds = serialize(form);

        fetch('/rest/uet-auth', {
            method: 'post',
            body: uetCreds,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(function(response) {
            console.log(response.json());
        });

        this.onShowAlert();

    }
    render(){
    return (
        <div className="FirstTab">
            <section className="text-center features-icons">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-4">
                            <div className="mx-auto features-icons-item mb-5 mb-lg-0 mb-lg-3"/>
                            <section className="login-clean form-login">
                                <form id="uet-auth-form">
                                    <h2 className="visually-hidden">Login Form</h2>
                                    <div className="illustration">
                                        <img src="uet-logo.png" alt={"UET Logo"}/>
                                    </div>
                                    <div className="mb-3">
                                        <input className="form-control" type="text"
                                             id="uet-username-1" name="username"
                                             placeholder="Username"/>
                                        </div>
                                    <div className="mb-3">
                                        <input className="form-control" type="password"
                                         id="uet-password-1" name="password"
                                         placeholder="Password"/>
                                    </div>
                                    <button className="btn btn-primary d-block w-100 btn-signin" type="button"
                                            onClick={this.submitLoginUetCourses}
                                    >Sign In</button>
                                    <div className="mb-3"/>
                                </form>
                            </section>
                        </div>
                        <div className="col-lg-4">
                            <div className="mx-auto features-icons-item mb-5 mb-lg-0 mb-lg-3"/>
                            <section className="login-clean form-login">
                                <form id="google-auth-form">
                                    <h2 className="visually-hidden">Login Form</h2>

                                    <div className="illustration">

                                        <img src="google.png" width="76" alt={"Chrome Logo"} className="google-img"/>

                                        <div className="gmail-ggcalendar-img">
                                            <img className="gmail-img" src="gmail-logo.png" width="76" alt={"Gmail Logo"}/>

                                            <img className="ggcalendar-img" src="google-calendar-logo.png" width="76" alt={"Google calendar Logo"}/>
                                        </div>


                                    </div>

                                    <div className="mb-3">

                                        <div id="g-signin2"
                                            className="justify-content-center align-items-center text-center"
                                            data-width="240" data-height="36" data-longtitle="true"/>
                                        {/*<button className="btn btn-primary d-block w-100 btn-signin" id="sign-in-google"*/}
                                        {/*        type="button">Sign In with Google*/}
                                        {/*</button>*/}
                                        <LoginButton/>
                                        <LogoutButton/>
                                    </div>
                                </form>
                            </section>
                        </div>
                    </div>
                </div>
            </section>


            <div>
                <Alert color="success" variant="success" show={this.state.visible}>
                    Đăng nhập vào UET courses thành công.
                    Thông tin đăng nhập đã được lưu vào hệ thống
                </Alert>
            </div>

            <script src="/src/bootstrap/js/bootstrap.min.js"/>
            <script src="/src/js/authen_func/auth-google.js"/>
            <script src="/src/js/authen_func/z-auth.js"/>
            <script src="/src/js/authen_func/authConfig.js"/>
            <script src="/src/js/authen_func/graphConfig.js"/>
            <script src="/src/js/authen_func/authPopup.js"/>
            <script src="/src/js/authen_func/graph.js"/>
        </div>



        );
    }
};
export default LoginTab;
