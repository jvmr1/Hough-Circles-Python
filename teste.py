import numpy as np

raio=3
lin=3
col=3

accumulator=np.zeros((raio, lin, col))

accumulator[0][1][0]=0.98
accumulator[1][1][1]=1.0
accumulator[2][1][2]=0.99

print(accumulator)
print('---------------------')

dtype = [('raio', int), ('lin', int), ('col', int), ('acc', float)]

values=[]

for r in range(raio):
    for l in range(lin):
        for c in range(col):
            values.append((r, l, c, accumulator[r][l][c]))
print(values)

coord = np.array(values, dtype=dtype)

coord=np.sort(coord, order='acc')

print(coord)
