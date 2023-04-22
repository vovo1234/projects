import sys
import os
import argparse

def collect_truth_entries(truth_folder, main_folder):
    columns = []
    truth_entries = {}
    
    print("  Collecting entries from %s..." % truth_folder)
    for file in os.scandir(truth_folder):
        if not file.is_file():
            continue
        if not file.path.endswith(".txt"):
            continue
        # get the filename, trim .txt suffix
        column = os.path.split(file.path)[1][:-4] 
        if column == "file":
            raise(Exception("the word 'file' is reserved and cannot be used as a column"))
        columns.append(column)
        with open(file.path, "r") as fs:
            #c = len(fs.readlines())
            #print(file.path, c)
            #values_by_filename = {line.split(",")[0]: line.split(",")[1] for line in fs.readlines()}
            #column_values[column] = values_by_filename
            for line in fs.readlines():
                tokens = line.split(",")
                filepath = os.path.relpath(os.path.join(truth_folder, tokens[0]), start = main_folder)
                truth_entry_values = {}
                if filepath in truth_entries:
                    truth_entry_values = truth_entries[filepath]
                else:
                    truth_entries[filepath] = truth_entry_values
                truth_entry_values[column] = tokens[1].strip()

    columns.sort()
    return columns, truth_entries

def save_aggregated_truth(truth_entries, columns, folder):
    truth_filename = os.path.join(folder, "truth.txt")
    print("  Writing to file %s..." % truth_filename)
    with open(truth_filename, "w") as fs:
        comment = "#file," + ",".join(columns) + "\n"
        fs.write(comment)
        filenames = list(truth_entries.keys())
        filenames.sort()
        for filename in filenames:
            vals = [filename]
            line = ",".join(vals)
            for column in columns:
                vals.append(truth_entries[filename][column])
            line = ",".join(vals)
            
            fs.write(line + "\n")
    
def aggregate_truth(folder):
    aggregated_truth_entries = {}
    columns = None
    for entry in os.scandir(folder):
        if entry.is_dir():
            #print(os.path.relpath(entry.path, start = folder))
            folder_columns, truth_entries = collect_truth_entries(entry.path, folder)
            if len(folder_columns) != 0:
                if columns is None:
                    columns = folder_columns
                else:
                    if columns != folder_columns:
                        raise(Exception("columns mismatch: %s != %s" % (str(columns), str(folder_columns))))
            aggregated_truth_entries |= truth_entries
    
    print(aggregated_truth_entries[list(aggregated_truth_entries.keys())[0]])    
    save_aggregated_truth(aggregated_truth_entries, columns, folder)
    

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('folder')

    args = parser.parse_args()
    aggregate_truth(args.folder)

if __name__ == "__main__":
    main()
