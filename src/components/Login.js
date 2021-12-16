import React from "react";
import { Link } from "react-router-dom";
import LoginButton from "./google.login/LoginButton";
export default class Login extends React.Component {
    render() {
        return (
            <form>
                <h3>Login</h3>
                <div className="form-group">
                    <label>Username</label>
                    <input type="username" className="form-control" onChange={this.setParams} placeholder="Enter username" />
                    <label>Password</label>
                    <input type="password" className="form-control" placeholder="Enter password" />
                </div>
                <div className="form-control__submit2" >
                    <button type="submit" style={{margin:'10px',border:'1px solid gray', borderRadius: '10px'}}>
                        Confirm
                    </button>
                    <button type="submit" onClick={this.login} style={{border:'1px solid gray',borderRadius: '10px'}} >  
                        <Link style={{textDecoration:'none', color:'black'}} to="/sign-up">Chưa có tài khoản? Đăng Ký!</Link>
                    </button>
                </div>
                <div className="form-control__submit3">
                    <button style={{color:'white',backgroundColor:'#1E90FF',width:'250px',margin:'10px',border:'1px solid gray', borderRadius: '10px'}}>
                        Sign in with UETCourses         
                    </button>
                    <button style={{color:'white',backgroundColor:'#CD5C5C',width:'250px',margin:'10px',border:'1px solid gray', borderRadius: '10px'}}>
                        <LoginButton/>          
                    </button>
                </div>       

            </form>
        );
    }
}
