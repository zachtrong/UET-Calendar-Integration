/* eslint-disable jsx-a11y/alt-text */
import React from "react";
import { Link } from "react-router-dom";
class RegisterTab extends React.Component{
    render(){
    return (
        <div class="container">
            <div class="row">
            <div class="col-lg-10 col-xl-9 mx-auto">
                <div class="card card-signin flex-row my-5">
                <div class="card-img-left d-none d-md-flex">
                </div>
                <div class="card-body">
                    <h5 class="card-title text-center">Register</h5>
                    <form class="form-signin">
                    <div class="form-label-group">
                        <input type="text" id="inputUserame" class="form-control" placeholder="Username" required autofocus/>
                        <label for="inputUserame">Username</label>
                    </div>
        
                    <div class="form-label-group">
                        <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required/>
                        <label for="inputEmail">Email address</label>
                    </div>
                    
                    <hr/>
        
                    <div class="form-label-group">
                        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required/>
                        <label for="inputPassword">Password</label>
                    </div>
                    
                    <div class="form-label-group">
                        <input type="password" id="inputConfirmPassword" class="form-control" placeholder="Password" required/>
                        <label for="inputConfirmPassword">Confirm password</label>
                    </div>
        
                    <button class="btn btn-lg btn-primary btn-block text-uppercase" 
                        style={{color: 'white',
                            background:'#0099ff',
                            width:'450px',
                            marginLeft:'5px',
                            paddingLeft:'180px'
                        }}
                        type="submit">
                        Register
                    </button>
                    <Link style={{textDecoration: 'none', fontSize:'20px'}} to="/login" class="d-block text-center mt-2 small">Log In</Link>
                    <hr class="my-4"/>
                    <Link style={{textDecoration: 'none'}} to="/login">
                    <button class="btn btn-lg btn-facebook btn-block text-uppercase"
                        style={{color: 'white',
                                background:'red',
                                width:'450px',
                                marginLeft:'5px',
                                margin:'5px',
                                paddingLeft:'135px'
                            }}
                        type="button">
                        Sign in with Google
                    </button>
                    </Link>
                    <button class="btn btn-lg btn-facebook btn-block text-uppercase"
                        style={{color: 'white',
                                background:'#4267b2',
                                width:'450px',
                                marginLeft:'5px',
                                paddingLeft:'125px'
                            }}
                        type="button">
                        Sign up with Facebook
                    </button>
                    </form>
                </div>
                </div>
            </div>
            </div>
        </div>
        
        
        );
    }
};
export default RegisterTab;