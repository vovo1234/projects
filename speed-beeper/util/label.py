import sys
import os

def main():
    #print("args.len()", len(sys.argv))
    #for a in sys.argv:
    #    print("  %s" % a)
    if len(sys.argv) < 2:
        print("ERROR: Truth filepath is not provided")
        exit()
        
    truth = sys.argv[1]

    if not os.path.exists(truth):
        print("crearing new file %s" % truth)
        with open(truth, "w") as f:
            f.close()
    else:
        print("opening existing file %s" % truth)

    lines = []
    count = 0 
    with open(truth, 'r') as f:
        lines = f.readlines()
        print("%s lines in file" % len(lines))
        f.close()

        if len(lines) > 0:
            line = lines[-1]
            count = int(line.split(".")[0]) + 1

    with open(truth, "a") as f:
        prevValue = ""
        while True:
            value = input("%04d:" % count)
            if len(value) == 0:
                value = prevValue
            f.write("%04d.jpg,%s\n" % (count, value))
            prevValue = value
            count += 1
        
    #print("%04d: " % count)

    #with open(truth, "a"):

if __name__ == "__main__":
    main()
