import socket
import sys
import IN
import struct
import time

my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
#my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
my_socket.bind(('192.168.210.173',5004))

print ("I am Pi 192.168.210.173")
#my_socket.setsockopt(socket.SOL_SOCKET, IN.SO_BINDTODEVICE, "wlan0")
#my_socket.setsockopt(socket.SOL_SOCKET,IN.SO_BINDTODEVICE,struct.pack("%ds" %(len("wlan0")+1,), "eth0"))
#PACKETDATA = 'f1a525da11f6'.decode('hex')
#my_socket.send(PACKETDATA)

for i in range(4):
        my_socket.sendto("173 Pi", ('<broadcast>' ,5004))
print ("Sent Packet")

while 1:
        message, address = my_socket.recvfrom(1024)
        print ("Messag: " + message + str(address))
        #print ("Received from: " + str(address))

