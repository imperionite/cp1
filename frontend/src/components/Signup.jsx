// eslint-disable-next-line react/prop-types
function Signup({ onSwitch }) {
    return (
        <div className="form-container">
            <h2>Sign Up</h2>
            <form>
                <label htmlFor="name">Name:</label>
                <input type="text" id="name" required />
                
                <label htmlFor="email">Email:</label>
                <input type="email" id="email" required />
                
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" required />
                
                <button type="submit">Sign Up</button>
            </form>
            <p>Already have an account? <button onClick={onSwitch}>Login</button></p>
        </div>
    );
}

export default Signup;