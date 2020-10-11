import cookies from "next-cookies";
import axios from "axios";

import NavBar from "../components/navbar";

import {Container, Row, Col, Card, Image, Button} from 'react-bootstrap';

class Profile extends React.Component {
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
                <Container fluid>
                    <Card className="text-center" style={ {border: '0px solid rgba(0,0,0,.125)'}}>
                        <Card.Body>
                            <Image src={avatar(this.props.user.id, this.props.user.avatar)} roundedCircle />
                            <h1 className="text-center">{this.props.user.username}#{this.props.user.discriminator}</h1>
                            <p>Always working on new ideas...</p>
                            <Button variant="primary">Edit profile</Button>
                        </Card.Body>
                    </Card>
                </Container>
            </div>
        )
    }
}

function avatar(userid, avatarid) {
    let url = "https://cdn.discordapp.com/avatars/" + userid + "/" + avatarid + ".png?size=128"
    return url;
}

export default Profile;