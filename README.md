<p align="center">
<img src="https://media-exp1.licdn.com/dms/image/C510BAQEut625xhgL8w/company-logo_200_200/0?e=1595462400&v=beta&t=34DYAra_uBHEmaJjV_iOaaZnroFxQ2csZgXwJsco7rg"  width="150" height="150">  
</p> 

<h4 align="center">Android p2p cdn sdk to distribute load and reduce costs(https://grow.peervadoo.com/)</h4>

<p align="center">
<a href="https://android-arsenal.com/api?level=19"><img src="https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat" alt="api"></a>        
</p>  

Vadoo is a p2p sdk integration to reduce your video streaming costs by upto 30% and scale to 6x the number of users

<p align="center">
<img src="Vadoo_player.jpg"  width="300" height="500">
</p>

Vadoo is powered by webrtc which distributes the load on users to reduce the server costs and to improve scalability

**First 10 GB data free every month using P2P(Peer 2 Peer) network** 

## Features
- Support live and VOD streams over HLS protocol(m3u8)
- Support encrypted HLS stream
- Support cache to avoid repeating the download of TS file
- Very easy to integrate with an existing Android and Android TV project
- Support any Android player
- Efficient scheduling policies to enhance the performance of P2P streaming

## Integration

To integrate with our sdk, follow the steps from here [Integration](Integration.md)

## Sample apps

To test out our sdk you can follow the examples from [Sample apps](sample_apps). 

The sample apks display on the screen the bandwidth savings you get by distributing load on the peers 

## Token generation

Make a POST request to api.peervadoo.com/get_token?email=Your-email-id

A token will be sent to your email id. Click on verification link in email to verify and start using the token.

## Bandwidth Savings info

Get the token from above step

Make a GET request to api.peervadoo.com/get_stats?token=Your-token

The response gives how much of consumption is done through http and how much you saved with p2p

## Support

Raise an issue for any additional feature request which will be actively worked upon

## Contributors

[Anil](https://gitlab.com/matcha72)

[Ankur](https://github.com/ncodepro)

## Contact Us
Email：vadootv@gmail.com