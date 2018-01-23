import pandas as pd
import matplotlib.pyplot as plt


 #Graph-1
df = pd.read_csv("graph1.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("λ")
plt.ylabel("Average delay")
plt.show()


#Graph-1
df = pd.read_csv("graph2.txt", sep=' ')
df.columns=['y','x']
y = df['y'].tolist()
x = df['x'].tolist()
plt.plot(x,y)
plt.xlabel("λ")
plt.ylabel("Average queue size")
plt.show()


#Graph-3
df = pd.read_csv("graph3.txt", sep=' ')
df.columns=['a','b', 'c', 'd', 'e']
a = df['a'].tolist()
b = df['b'].tolist()
c = df['c'].tolist()
d = df['d'].tolist()

data = [a, b, c, d]

plt.boxplot(data)

#plt.plot(x,y)
#plt.xlabel("λ")
#plt.ylabel("Average queue size")
plt.show()


#Graph-4
df = pd.read_csv("graph4.txt", sep=' ')
df.columns=['a','b', 'c', 'd', 'e']
a = df['a'].tolist()
b = df['b'].tolist()
c = df['c'].tolist()
d = df['d'].tolist()

data = [a, b, c, d]

plt.boxplot(data)

#plt.plot(x,y)
#plt.xlabel("λ")
#plt.ylabel("Average queue size")
plt.show()
