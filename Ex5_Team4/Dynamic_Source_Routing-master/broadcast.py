import socket
import sys
import time
import pickle

pathList = list()
my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
#my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
my_socket.bind(('192.168.210.170',5004))

print ("I am Pi 192.168.210.170")
RREQ_Packet ="RREQ**101**192.168.210.170**" + "192.168.210.193"
# RREQ Packet --> RREQ + Packet ID + Source IP + Dest IP 
msgToTransfer = "Testing DSR"
for i in range(4):
        my_socket.sendto(RREQ_Packet, ('<broadcast>' ,5004))
print ("Sent RREQ Packet")

my_socket.settimeout(10)
# Unicast is done infinitely. So reading only first 10 unicast 
for i in range(2):
	try:
		message, address = my_socket.recvfrom(1024)
		#print ("Message: "+ str(message) )
		if ('RREP' in message):
			print ("Received RREP")
			#message = pickle.loads(message)	
			#print ("Path Found: " + str(message) )
			if message not in pathList:	
				print ("Adding Path found to the ROute cache")
				pathList.append(message)

	except:
		pass

# Now paths from source to destination is available
# Preprocessing the received Path

# Send a packet from source to destination 
print ("Path" +str(pathList[0]))
srcToDes = pathList[0] 
Packet = [x.strip() for x in srcToDes.split('**')] 
Packet.append(msgToTransfer)
del Packet[0]
del Packet[0]
print "Packet" + str(Packet)
MESSAGE = pickle.dumps(Packet)
# Serialised the packet using pickle


#Send a packet
my_socket.close()
time.sleep(1)
my_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
my_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
my_socket.bind(('',5004))

if (len(Packet) !=3 ):
	MESSAGE = pickle.loads(MESSAGE)	
	print ("Before List Comprehension: " + str(MESSAGE))
	ToAddress = Packet[2]	
	#del Packet[2]
	MESSAGE = pickle.dumps(Packet)	
	my_socket.sendto(MESSAGE,(ToAddress,5004))
	


