import React from 'react';
import { useGoogleLogout } from 'react-google-login';

const clientId = '950594419536-4pvhe5a9p1e4lq09h40s5p6j3ilr7h4u.apps.googleusercontent.com';

function LogoutButton(){
    const onLogoutSuccess = (res) => {
        alert('Loogged out Successfully');
    };
    const onFailure = () => {
        console.log('Handle failure success');
    };

    const { signOut } = useGoogleLogout({
        clientId,
        onLogoutSuccess,
        onFailure
    });


    return (
        // <button onClick={signOut}>Logout</button>
        <button className="btn btn-primary d-block w-100 btn-signin" id="sign-in-google"
                type="button"
                onClick={signOut}
        >Sign out
        </button>
    );
};

export default LogoutButton;