import sys
import os

if len(sys.argv) < 2:
    print("ERROR directory is not provided")
    exit()

count = 0
dir = sys.argv[1]
for f in os.listdir(dir):
    if len(f) == 7 and f.endswith(".jpg"):
        nf = "0" + f
        print("  %s -> %s" % (os.path.join(dir, f), os.path.join(dir, nf)))
        os.rename(os.path.join(dir, f), os.path.join(dir, nf))
        
    count += 1
    #if count > 5:
    #    break

print("%d files renamed" % count)
    
