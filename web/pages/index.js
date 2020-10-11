import {Navbar, Nav, Container, Card} from "react-bootstrap";

import axios from 'axios'
import NavBar from "../components/navbar";
import cookies from "next-cookies";

class Home extends React.Component {
    static async getInitialProps(ctx) {
        let token = cookies(ctx).access_token
        let userInfo = null;
        await axios.get('http://127.0.0.1:4000/user/info', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
        ).then(response => { userInfo = response.data })
            .catch(error => { console.log("didn't work...")  })

        return {
            user: userInfo,
            token: cookies(ctx).access_token
        }
    }

    render() {
        return (
            <div>
                <NavBar userinfo={this.props.user}/>

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

