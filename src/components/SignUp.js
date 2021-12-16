import React from "react";
import {Link} from "react-router-dom";
export default class SignUp extends React.Component {
    render() {
        return (
            <form>
                <h3>Register</h3>
                <div className="form-group">
                    <label>Username</label>
                    <input type="username" className="form-control" placeholder="Enter username" />
                    <label>Password</label>
                    <input type="password" className="form-control" placeholder="Enter password" />
                </div>
                <div className="form-control__submit" >
                    <button type="submit" style={{margin:'10px',border:'1px solid gray', borderRadius: '10px',color:'black'}}>
                        Confirm
                    </button>
                    <button type="submit" style={{border:'1px solid gray',borderRadius: '10px'}} >  
                        <Link style={{textDecoration:'none', color:'black'}} to="/sign-in">Đã có tài khoản? Đăng Nhập</Link>
                    </button>
                </div>
            </form>
        );
    }
}