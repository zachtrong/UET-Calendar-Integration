/* eslint-disable react/jsx-no-undef */
import React from 'react';
import '../css/App2.css';
import { Link } from 'react-router-dom';
export default function Tabs() {
    const tabsStyle={
        color:'white',
        textDecoration: 'none'
    };
    return (
        <tabs>
            <ul className="nav-links">
                <Link style={tabsStyle} to="/login">
                    LOGIN
                </Link>
            </ul>
        </tabs>    
        );
};