import sys

with open(sys.argv[1]) as f:
	for line in f:
		line = line.rstrip()
		print(line)
		print(line + ' in {Quarter}')
		print(line + ' in {Quarter} {Year}')