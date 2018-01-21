import pandas as pd
import matplotlib.pyplot as plt

 
df = pd.read_csv("graph1.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("utilisation factor")
plt.ylabel("drop rate")
plt.show()
