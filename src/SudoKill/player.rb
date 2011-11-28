#! /usr/local/bin/ruby

# Sample network player for SudoKill

require "socket"

class Player
  # @param socket [BasicSocket] The socket connection to the game server.
  # @param name [String] Name of this player
  def initialize(socket, name)
    @out = socket
    @name = name

    @out.puts PLAYER_CODE
    @out.puts name
  end
  
  # Executes a move and sends it to the game server.
  #
  # @param x [Number] x coordinate of the position to set a number
  # @param y [Number] y coordinate of the position to set a number
  # @param n [Number] The number to set.
  def move(x, y, n)
    @out.puts "#{x} #{y} #{n}"
  end

  private
  PLAYER_CODE = "SUDOKILL_PLAYER"
end

class Map
  # @param socket [BasicSocket]
  def initialize(socket)
    @in = socket
    @map = new_map
  end

  # Reads from the data source the latest state and update the map.
  #
  # @return the hash where :x and :y are the coordinates while :n is the number in string
  def update
    x, y, n = -1, -1, -1

    @map = new_map
    line = @in.readline # MOVE START
    line = @in.readline.strip # Get rid of newlines

    while line != MOVE_END do
      x, y, n = line.split(" ")
      @map[y.to_i][x.to_i] = n unless n == "-1"
      line = @in.readline.strip # Get rid of newlines
    end

    { :x => x.to_i, :y => y.to_i, :n => n.to_i }
  end

  def to_s
    @map.join("\n")
  end

  private
  MOVE_START = "MOVE START"
  MOVE_END = "MOVE END"
  LENGTH = 9
  WIDTH = 9

  # Create a new empty map.
  def new_map
    map = []
    LENGTH.times { map << ("_" * WIDTH) }

    map
  end
end

#######################################
# Script starts here

if ARGV.size != 2 then
  puts "usage: #{__FILE__} <port> <name>"
else
  HOST = "localhost"
  PORT = ARGV[0].to_i
  NAME = ARGV[1]

  socket = TCPSocket.open(HOST, PORT)

  player = Player.new(socket, NAME)
  map = Map.new(socket)

  loop do
    last_move = map.update
    puts map
    puts "Last Move: #{last_move[:n]} @ #{last_move[:x]}, #{last_move[:y]}"

    print "Please enter location (x, y): "
    x, y = STDIN.gets.split(",").map { |x| x.to_i }

    print "Please enter number (1-9): "
    num = STDIN.gets.to_i

    player.move(x, y, num)
  end
end

