# BitTorrent client
<b>Authors:</b> Niesuch, Robsonner, PawelBanasiuk, Lukasz-Grzebula, Pawonashi <br />
<b>Programming language:</b> JAVA <br />
<b>In order to:</b> Final project regarding subject "Parallel and Distributed Processing" <br />

## Table of Contents
1. [Description](https://github.com/niesuch/bittorrentclient/blob/master/README.md#description)
2. [About program](https://github.com/niesuch/bittorrentclient/blob/master/README.md#about-program)
	* [Layout](https://github.com/niesuch/bittorrentclient/blob/master/README.md#layout)
3. [Design Assumptions](https://github.com/niesuch/bittorrentclient/blob/master/README.md#desing-assumptions)
4. [Links](https://github.com/niesuch/bittorrentclient/blob/master/README.md#links)
5. [Installation](https://github.com/niesuch/bittorrentclient/blob/master/README.md#installation)
6. [Changelogs](https://github.com/niesuch/bittorrentclient/blob/master/README.md#changelogs)
7. [Copyright and license](https://github.com/niesuch/bittorrentclient/blob/master/README.md#copyright-and-license)

## Description
Bittorrentclient is a program which use to download torrents. BitTorrent is a communications protocol for the practice of peer-to-peer file sharing that is used to distribute large amounts of data over the Internet.

The BitTorrent protocol can be used to reduce the server and network impact of distributing large files. Rather than downloading a file from a single source server, the BitTorrent protocol allows users to join a "swarm" of hosts to upload to/download from each other simultaneously. The protocol is an alternative to the older single source, multiple mirror sources technique for distributing data, and can work effectively over networks with lower bandwidth. Using the BitTorrent protocol, several basic computers, such as home computers, can replace large servers while efficiently distributing files to many recipients. This lower bandwidth usage also helps prevent large spikes in internet traffic in a given area, keeping internet speeds higher for all users in general, regardless of whether or not they use the BitTorrent protocol.

A user who wants to upload a file first creates a small torrent descriptor file that they distribute by conventional means (web, email, etc.). They then make the file itself available through a BitTorrent node acting as a seed. Those with the torrent descriptor file can give it to their own BitTorrent nodes, which—acting as peers or leechers—download it by connecting to the seed and/or other peers (see diagram on the right).

Segmented file transfer implementation: the file being distributed is divided into segments called pieces. As each peer receives a new piece of the file it becomes a source (of that piece) for other peers, relieving the original seed from having to send that piece to every computer or user wishing a copy. With BitTorrent, the task of distributing the file is shared by those who want it; it is entirely possible for the seed to send only a single copy of the file itself and eventually distribute to an unlimited number of peers.

Each piece is protected by a cryptographic hash contained in the torrent descriptor. This ensures that any modification of the piece can be reliably detected, and thus prevents both accidental and malicious modifications of any of the pieces received at other nodes. If a node starts with an authentic copy of the torrent descriptor, it can verify the authenticity of the entire file it receives.

Pieces are typically downloaded non-sequentially and are rearranged into the correct order by the BitTorrent Client, which monitors which pieces it needs, and which pieces it has and can upload to other peers. Pieces are of the same size throughout a single download (for example a 10 MB file may be transmitted as ten 1 MB pieces or as forty 256 KB pieces). Due to the nature of this approach, the download of any file can be halted at any time and be resumed at a later date, without the loss of previously downloaded information, which in turn makes BitTorrent particularly useful in the transfer of larger files. This also enables the client to seek out readily available pieces and download them immediately, rather than halting the download and waiting for the next (and possibly unavailable) piece in line, which typically reduces the overall time of the download.

Once a peer has downloaded a file completely, it becomes an additional seed. This eventual transition from peers to seeders determines the overall "health" of the file (as determined by the number of times a file is available in its complete form).

The distributed nature of BitTorrent can lead to a flood-like spreading of a file throughout many peer computer nodes. As more peers join the swarm, the likelihood of a completely successful download by any particular node increases. Relative to traditional Internet distribution schemes, this permits a significant reduction in the original distributor's hardware and bandwidth resource costs.

* Source: [Wikipedia - Bittorrent](https://en.wikipedia.org/wiki/BitTorrent)

## About program
#### Layout
Layout was written in Swing (GUI for JAVA). It can be used on any operating system.

Layout consists of the functionalities, such as:
* Menubar (JMenuBar) which give the program drop-down menu where is located functions to open torrent files, change columns view (show/hide) and informations about authors, application version, github url.
* Table (JTable) where is informations about current downloads, for example: torrent name, size, % downloaded, status, download speed, upload speed, time remaining, etc.
* Right panel (JPanel) with additional informations about our torrent. It can be collapse if user doesn't want it.
* Buttons panel (JButton) consisting of four buttons which are used to perform torrent actions (Pause, Resume, Cancel, Delete).

## Design Assumptions
1. Using camelCase.
2. Brackets ({ }) adding in next line.
3. Functions shouldn't have more than 60 lines of code but we can have exceptions.
4. Using english in code and commits.
5. In functions names using camelCase too, for example functionDoSomething().
6. Adding comments with understandable descriptions behind functions.
7. If condition/loop has one line of code adding brackets all the same.
8. Private variables and private methods preceding "_".
9. Taking care of formatting code.
10. Adding comments in difficult functions.

## Links

## Installation
Bittorrentclient doesn't require installation. To use program you must have Java in version JRE 7+ and any operating system. So you can compile the code and enjoy it.

## Changelogs
Click on this link to see changelogs: [CHANGELOG](https://github.com/niesuch/bittorrentclient/releases)

## Copyright and license
Copyright 2015. Code released under the [MIT license](https://github.com/niesuch/bittorrentclient/blob/master/LICENSE.md).

[Back to top](https://github.com/niesuch/bittorrentclient/blob/master/README.md#bittorrent-client)
