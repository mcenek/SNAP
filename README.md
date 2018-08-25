# SNAP
Semantic Network Analysis Project

## Setup

### Email Registration

The email registration sends via the php email library, using the `sendmail` application, so if you need to have said feature working, you'll need to ensure that is installed:

`sudo apt install sendmail` (or whatever your system's equivalent is)

Sendmail requires a fully qualified domain name to work correctly, so locate your `hosts` file (`/etc/hosts` on ubuntu 18.04), and add something like the following:

`127.0.0.1 snap.dev <hostname>`

where `<hostname>` is replaced with your machine's hostname.

Note: This was true for a dev machine running `Ubuntu 18.04.1 LTS`
Mail config may depend on os, machine, and version.
