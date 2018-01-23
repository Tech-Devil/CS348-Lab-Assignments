import pandas as pd
import matplotlib.pyplot as plt


#Graph-1
df = pd.read_csv("graph1.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("Utilisation factor")
plt.ylabel("Average delay")
plt.show()


#Graph-1
df = pd.read_csv("graph2.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("Utilisation factor")
plt.ylabel("Packet loss rate")
plt.show()
