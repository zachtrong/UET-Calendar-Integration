import React from 'react';
import { useGoogleLogin } from 'react-google-login';
import { refreshTokenSetup  } from './refreshTokenSetup';

const clientId ='950594419536-4pvhe5a9p1e4lq09h40s5p6j3ilr7h4u.apps.googleusercontent.com';

function LoginButton(){
    const onSuccess = (res) => {
        console.log('Login Success:currentUser:' ,res);
        refreshTokenSetup(res);
    };
    const onFailure = (res) => {
        console.log('Login failed: res:', res );
    };

    const { signIn } = useGoogleLogin({
        clientId,
        onSuccess,
        onFailure,
        isSignedIn:true,
        accessType:'offline'
    });

    return (
        <h7 id="sign-in-google" type="submit" onClick={signIn}>
            Sign In with Google
        </h7>
    );
};

export default LoginButton;