def empty_boxes(board):
    counter = 0
    for i in board:
        for j in i:
            if j == 0:
                counter += 1
    if counter == 0:
        return False
    else:
        return True


def is_valid(x, row, column, row_index, column_index):
    # check row
    row_counter = 0
    for y in row:
        if x == y:
            row_counter += 1
    if row_counter != 0:
        return False

    # check column
    col_counter = 0
    for y in column:
        if x == y:
            col_counter += 1
    if col_counter != 0:
        return False

    # check box
    box_counter = 0
    x_of_box = column_index // 3
    y_of_box = row_index // 3
    for p in range(y_of_box * 3, y_of_box * 3 + 3):
        for q in range(x_of_box * 3, x_of_box * 3 + 3):
            if board[p][q] == x:
                box_counter += 1
    if box_counter != 0:
        return False

    return True


def find_empty(board):
    for i in range(len(board)):
        for j in range(len(board[0])):
            if board[i][j] == 0:
                return i, j


def find_column(board, column):
    return [row[column] for row in board]


def solve(board):
    hold = find_empty(board)
    if not hold:
        return True
    else:
        row, column = hold
        for x in range(1, 10):
            if is_valid(x, board[row][:], find_column(board, column), row, column):
                board[row][column] = x
                if solve(board):
                    return True
                board[row][column] = 0
        return False


"""board = [[8, 0, 0, 4, 0, 6, 0, 0, 7],
         [0, 0, 0, 0, 0, 0, 4, 0, 0],
         [0, 1, 0, 0, 0, 0, 6, 5, 0],
         [5, 0, 9, 0, 3, 0, 7, 8, 0],
         [0, 0, 0, 0, 7, 0, 0, 0, 0],
         [0, 4, 8, 0, 2, 0, 1, 0, 3],
         [0, 5, 2, 0, 0, 0, 0, 9, 0],
         [0, 0, 1, 0, 0, 0, 0, 0, 0],
         [3, 0, 0, 9, 0, 2, 0, 0, 5]]"""
board = [[8, 0, 0, 0, 0, 0, 0, 0, 0],
         [0, 0, 3, 6, 0, 0, 0, 0, 0],
         [0, 7, 0, 0, 9, 0, 2, 0, 0],
         [0, 5, 0, 0, 0, 7, 0, 0, 0],
         [0, 0, 0, 0, 4, 5, 7, 0, 0],
         [0, 0, 0, 1, 0, 0, 0, 3, 0],
         [0, 0, 1, 0, 0, 0, 0, 6, 8],
         [0, 0, 8, 5, 0, 0, 0, 1, 0],
         [0, 9, 0, 0, 0, 0, 4, 0, 0]]


# board = [[0, 0, 0, 0, 0, 0, 0, 0, 0],
#                 [1, 0, 8, 4, 5, 0, 7, 3, 9]]
# print(sudoku_example[0][4])
solve(board)
for i in board:
    for j in i:
        print(j, end=" ")
    print()
