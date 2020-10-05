import {Navbar, Nav, Container, Card} from "react-bootstrap";

import NavBar from "../components/navbar";

class Home extends React.Component {
    render() {
        return (
            <div>
                <NavBar />

                <Container>
                    <br />
                    <Card>
                        <Card.Body>
                            <h1>Welcome!</h1>
                            <p>Welcome to the website where users are verified, theres nothing to interesting here besides verify your user in our discord server...
                            You can come back when you need to verify yourself!</p>
                        </Card.Body>
                    </Card>
                </Container>

            </div>
        )
    }
}

export default Home;

