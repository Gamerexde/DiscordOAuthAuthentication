import cookies from "next-cookies";
import axios from "axios";
import NavBar from "../../components/navbar";
import {Card, Container, Button} from "react-bootstrap";

class Join extends React.Component {
    static async getInitialProps(ctx) {
        let token = cookies(ctx).access_token
        let userInfo = null;
        await axios.get('http://127.0.0.1:4000/user/info', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        ).then(response => { userInfo = response.data })

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
                            <Card.Title>Join discord server.</Card.Title>
                            <p>Here are the steps to follow in order to join our discord server!</p>
                            <p><strong>(1)</strong> You will need to be logged in this website using Discord OAuth. Press the login button in the top or press this button:</p>
                            <Button variant="dark">Login with Discord</Button>
                            <p><strong>(2)</strong> Join our discord server, you will be redirected to the Verification Area after join.</p>
                            <iframe src="https://discordapp.com/widget?id=762340357571870723&theme=dark" width="350"
                                    height="500" allowTransparency="true" frameBorder="0"
                                    sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts">
                            </iframe>
                            <p><strong>(3)</strong> Verify your user here:</p>
                            <Button variant="dark" href="/discord/verify">Verify User</Button>
                        </Card.Body>
                    </Card>
                </Container>
            </div>
        )
    }
}
export default Join;