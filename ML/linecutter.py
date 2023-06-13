import os
import csv

folder_path = 'Dataset\Kota Surabaya'

def delete_lines(filename, num_lines):
    with open(filename, 'r') as file:
        lines = file.readlines()

    with open(filename, 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerows(csv.reader(lines[num_lines:]))

    print(f"The first {num_lines} line(s) have been deleted from {filename}.")

for filename in os.listdir(folder_path):
    if filename.endswith('.csv'):
        file_path = os.path.join(folder_path, filename)
        delete_lines(file_path, 3)
