# SNAP

Semantic Network Analysis Pipeline

## Setup

### Directory Permissions

For user files to be managed by the application and logs to be written, the server's user group needs to have access to both the base SNAP directory, and to the userfiles directory, wherever it is located on the host machine.

One of the easiest ways to achieve this on linux or mac is by setting the group of those directorys to whatever group the server is running under - something like `www-data` on ubuntu, ex: `sudo chgrp www-data SNAP-Users` - then set the group permissions of those folders to allow write access for the group, ex: `sudo chmod g+w SNAP-Users`.

On windows, this can be achieved by going to properties->security for that folder, then adding the server's group with the correct permissions.

TODO: What is the server's user group on Mac?

### Email Registration

The email registration sends via the php email library, using the `sendmail` application, so if you need to have said feature working, you'll need to ensure that is installed:

`sudo apt install sendmail` (or whatever your system's equivalent is)

Sendmail requires a fully qualified domain name to work correctly, so locate your `hosts` file (`/etc/hosts` on ubuntu 18.04), and add the following:

`127.0.0.1 uaanlp.org <hostname>`

where `<hostname>` is replaced with your machine's hostname.

Note: This was true for a dev machine running `Ubuntu 18.04.1 LTS`
Mail config may depend on os, machine, and version.
