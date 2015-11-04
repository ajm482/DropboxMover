# DropboxMover
The DropboxMover uses REST web services and the Dropbox API to search Dropbox for entries listed in a file called "_list" in a directory called "move."
The file format is as follows:

A.txt /home/abc123
B.txt /home/def456

A _list file with these two entries would move A.txt from Dropbox to /home/abc123/A.txt, and will move B.txt to /home/def456/B.txt
