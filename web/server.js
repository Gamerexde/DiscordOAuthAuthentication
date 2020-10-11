const express = require('express')
const axios = require('axios')
const qs = require('querystring')
const cookieParser = require('cookie-parser')
const cors = require('cors')
const mysql = require('mysql')
const bodyparser = require('body-parser')

const app = express()

app.use(bodyparser.json());
app.use(cookieParser())

app.use(
    cors({
        origin: 'http://127.0.0.1:3000',
        credentials: true
    })
)

let con = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: '123',
    database: 'DiscordOAuth'
})

const port = process.env.PORT || 4000

const clientID = "762340445388406805"
const secret = "IeffsaNbdmdqc4Qh8P6MAY6Uo5V0LW1J"

const redirectURL= "http://127.0.0.1:3000/verify"

app.listen(port, () => {
    console.log("Backend server has started! at http://127.0.0.1:" + port)
})

app.get('/', (req, res) => {
    res.send("Are you lost?")
})

app.get('/user/info', (req, res) => {
    const token = req.header('authorization')
    if (token) {
        axios.get('https://discordapp.com/api/users/@me', {
            headers: {
                Authorization: `${token}`
            }
        }).then(response => { res.json(response.data) })
            .catch(error => { res.sendStatus(401) })
    } else {
        res.sendStatus(401)
    }
})

app.get('/discord/verify', (req, res) => {
    const token = req.header('authorization')
    if (token) {
        axios.get('https://discordapp.com/api/users/@me', {
            headers: {
                Authorization: `${token}`
            }
        }).then(response => {
            let userid = response.data.id
            con.query({
                sql: "SELECT * FROM `oauth_discord` WHERE `USER_ID` LIKE ?",
                timeout: 40000,
                values: [userid]
            }, function (error, results, fields) {
                if (error) throw new Error(error)
                if (results[0].USER_ID === userid && results[0].isVerified === 0) {
                    con.query({
                        sql: "UPDATE `oauth_discord` SET `isVerified` = '1' WHERE `oauth_discord`.`USER_ID` = ?;",
                        timeout: 40000,
                        values: [userid]
                    }, function (error, results, fields) {
                        if (error) throw new Error(error)
                        res.json({
                            status: "ok"
                        })
                    });
                } else {
                    res.json({
                        status: "already"
                    })
                }
            });
        })
            .catch(error => { res.sendStatus(401) })
    } else {
        res.sendStatus(401)
    }
})

app.get('/verify', (req, res) => {
    let token = req.query.code

    axios.post('https://discord.com/api/oauth2/token', qs.stringify({
            client_id: clientID,
            client_secret: secret,
            grant_type: "authorization_code",
            code: token,
            redirect_uri: redirectURL,
            scope: "identify"
        }), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }

    ).then((response) => {
        if (response.status === 200) {
            let bearer = response.data.access_token

            axios.get('https://discordapp.com/api/users/@me', {
                headers: {
                    Authorization: `Bearer ${bearer}`
                }
            }).then(response => {
                let userid = response.data.id
                let username = response.data.username

                con.query({
                    sql: 'SELECT * FROM `oauth_users` WHERE `USER_ID` LIKE ?',
                    timeout: 40000,
                    values: [userid]
                }, function (error, results, fields) {
                    if (error) throw new Error(error)

                    if (!results[0]) {
                        con.query({
                            sql: 'INSERT INTO `oauth_users` (`USER_ID`, `USERNAME`) VALUES (?, ?);',
                            timeout: 40000,
                            values: [userid, username]
                        }, function (error, results, fields) {
                            if (error) throw new Error(error)
                        });
                        con.query({
                            sql: 'INSERT INTO `oauth_profile` (`USER_ID`, `USERNAME`, `BIO`, `COUNTRY`, `WEBSITE`, `DISCORD`, `YOUTUBE`, `TWITTER`, `TWITCH`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);',
                            timeout: 40000,
                            values: [userid, username, '', '', '', '', '', '', '']
                        }, function (error, results, fields) {
                            if (error) throw new Error(error)
                        });
                        con.query({
                            sql: 'INSERT INTO `oauth_discord` (`USER_ID`, `USERNAME`, `isVerified`, `isBanned`) VALUES (?, ?, ?, ?);',
                            timeout: 40000,
                            values: [userid, username, 0, 0]
                        }, function (error, results, fields) {
                            if (error) throw new Error(error)
                        });
                    }
                });
            }).catch(error => { })

            res.cookie('access_token', bearer, {
                maxAge: 3600000,
                httpOnly: true
            })
            res.sendStatus(200)
        } else {
            res.sendStatus(401)
        }

    }).catch(function (error) {
        res.sendStatus(401)
    });
})