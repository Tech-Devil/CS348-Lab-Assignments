import pandas as pd
import matplotlib.pyplot as plt

 
df = pd.read_csv("graph1.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("λ")
plt.ylabel("Average delay")
plt.show()

df = pd.read_csv("graph2.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("Average queue size")
plt.ylabel("λ")
plt.show()
