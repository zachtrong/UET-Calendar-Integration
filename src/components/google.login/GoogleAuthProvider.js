import React from 'react'
import { useGoogleLogin } from 'react-use-googlelogin'


const GoogleAuthContext = React.createContext()

export const GoogleAuthProvider = ({ children }) => {

    const googleAuth = useGoogleLogin({
        clientId: "950594419536-4pvhe5a9p1e4lq09h40s5p6j3ilr7h4u.apps.googleusercontent.com", // Your clientID from Google.
        // scope: "https://www.googleapis.com/auth/calendar.events.readonly https://www.googleapis.com/auth/gmail.readonly"
        // scope: "https://www.googleapis.com/auth/calendar.readonly",
    })

    return (
        <GoogleAuthContext.Provider value={googleAuth}>
            {children}
        </GoogleAuthContext.Provider>
    )
}

export const useGoogleAuth = () => React.useContext(GoogleAuthContext)