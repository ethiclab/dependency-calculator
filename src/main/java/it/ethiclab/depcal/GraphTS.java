package it.ethiclab.depcal;

import java.util.ArrayList;
import java.util.List;

public class GraphTS {
  private F vertexList[]; // list of vertices
  private int matrix[][]; // adjacency matrix
  private int numVerts; // current number of vertices
  private List<F> sortedArray;

  public GraphTS(int MAX_VERTS) {
    vertexList = new F[MAX_VERTS];
    matrix = new int[MAX_VERTS][MAX_VERTS];
    numVerts = 0;
    for (int i = 0; i < MAX_VERTS; i++)
      for (int k = 0; k < MAX_VERTS; k++)
        matrix[i][k] = 0;
    sortedArray = new ArrayList<>(); // sorted vert labels
  }

  public void addVertex(F f) {
    vertexList[numVerts++] = f;
  }

  public void addEdge(int start, int end) {
    matrix[start][end] = 1;
  }

  public List<F> topo() // toplogical sort
  {
    int orig_nVerts = numVerts; 

    while (numVerts > 0) // while vertices remain,
    {
      // get a vertex with no successors, or -1
      int currentVertex = noSuccessors();
      if (currentVertex == -1) // must be a cycle
      {
        throw new CircularReferenceException("ERROR: Graph has cycles");
      }
      // insert vertex label in sorted array (start at end)
      sortedArray.add(vertexList[currentVertex]);

      deleteVertex(currentVertex); // delete vertex
    }

    return sortedArray;
  }

  public int noSuccessors() // returns vert with no successors (or -1 if no such verts)
  { 
    boolean isEdge; // edge from row to column in adjMat

    for (int row = 0; row < numVerts; row++) {
      isEdge = false; // check edges
      for (int col = 0; col < numVerts; col++) {
        if (matrix[row][col] > 0) // if edge to another,
        {
          isEdge = true;
          break; // this vertex has a successor try another
        }
      }
      if (!isEdge) // if no edges, has no successors
        return row;
    }
    return -1; // no
  }

  public void deleteVertex(int delVert) {
    if (delVert != numVerts - 1) // if not last vertex, delete from vertexList
    {
      for (int j = delVert; j < numVerts - 1; j++)
        vertexList[j] = vertexList[j + 1];

      for (int row = delVert; row < numVerts - 1; row++)
        moveRowUp(row, numVerts);

      for (int col = delVert; col < numVerts - 1; col++)
        moveColLeft(col, numVerts - 1);
    }
    numVerts--; // one less vertex
  }

  private void moveRowUp(int row, int length) {
    for (int col = 0; col < length; col++)
      matrix[row][col] = matrix[row + 1][col];
  }

  private void moveColLeft(int col, int length) {
    for (int row = 0; row < length; row++)
      matrix[row][col] = matrix[row][col + 1];
  }

  public static void main(String[] args) {
    GraphTS g = new GraphTS(100);
    g.addVertex(new F("A", null)); // 0
    g.addVertex(new F("B", null)); // 1
    g.addVertex(new F("C", null)); // 2
    g.addVertex(new F("D", null)); // 3
    g.addVertex(new F("E", null)); // 4
    g.addVertex(new F("F", null)); // 5
    g.addVertex(new F("G", null)); // 6
    g.addVertex(new F("H", null)); // 7

    g.addEdge(0, 3); // AD
    g.addEdge(0, 4); // AE
    g.addEdge(1, 4); // BE
    g.addEdge(2, 5); // CF
    g.addEdge(3, 6); // DG
    g.addEdge(4, 6); // EG
    g.addEdge(5, 7); // FH
    g.addEdge(6, 7); // GH

    g.topo(); // do the sort
  }
}
