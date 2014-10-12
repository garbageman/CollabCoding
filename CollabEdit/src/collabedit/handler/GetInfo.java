package collabedit.handler;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import collabedit.Telnet;

public class GetInfo extends AbstractHandler {


	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	static IWorkspace workspace;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Telnet.init();	
		workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new IResourceChangeListener(){

			@Override
			public void resourceChanged(IResourceChangeEvent event){
				//change("Project1", "package1", "Class1.java");
				try{
				change();
				}
				catch(Exception e){
					System.out.println(e);
				}
				
			}
			
		});
		
		return null;
	}

	private void change(){
IWorkspaceRoot root = workspace.getRoot();
		try{
		IProject project1 = root.getProject("Project1");
		IFile file1 = project1.getFile("src/package1/Class1.java");
		BufferedInputStream in = new BufferedInputStream(file1.getContents());
		

		
		byte[] contents = new byte[1024];

		 int bytesRead=0;
		 String s = ""; 
		 while( (bytesRead = in.read(contents)) != -1){ 
		    s = new String(contents, 0, bytesRead);               
		 }
		 System.out.print(s);
		 
		

		makeAST(s);//, to);
		}catch(Exception e){
			System.out.println(">:(" + e);
		}
	}
	
	private void makeAST(String from)//, ICompilationUnit to)
			throws JavaModelException, IllegalArgumentException,
			MalformedTreeException, BadLocationException {
		String s = from;
		System.out.println(s);
		//to.getBuffer().setContents(s);
		Telnet.post(s);

	}

	public static void setCode(String s)
			throws JavaModelException {
		System.out.println(s);
		IWorkspaceRoot root = workspace.getRoot();
		IProject project1 = root.getProject("Project1");
		IPackageFragment[] packages1 = JavaCore.create(project1)
				.getPackageFragments();
		IPackageFragment pack1 = null;
		for (int i = 0; i < packages1.length; i++) {
			if (packages1[i].getElementName().equals("package1")) {
				pack1 = packages1[i];
				break;
			}

		}
		pack1.getCompilationUnit("Class1.java").getBuffer().setContents(s);
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 * 
	 * @param unit
	 * @return
	 * @throws BadLocationException 
	 * @throws MalformedTreeException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 * @throws CoreException 
	 */

	public void getCurrentEditorContent() throws IllegalArgumentException, MalformedTreeException, BadLocationException, IOException, CoreException {
		IWorkspaceRoot root = workspace.getRoot();
		
		IProject project1 = root.getProject("Project1");
		IFile file1 = project1.getFile("src/package1/Class1.java");
		BufferedInputStream in = new BufferedInputStream(file1.getContents());
		

		IProject project2 = root.getProject("Projcet2");
		IPackageFragment[] packages2 = JavaCore.create(project2)
				.getPackageFragments();
		IPackageFragment pack2 = null;
		for (int i = 0; i < packages2.length; i++) {
			if (packages2[i].getElementName().equals("package1")) {
				pack2 = packages2[i];
				break;
			}

		}
		
		byte[] contents = new byte[1024];

		 int bytesRead=0;
		 String s = ""; 
		 while( (bytesRead = in.read(contents)) != -1){ 
		    s = new String(contents, 0, bytesRead);               
		 }
		 System.out.print(s);
		 in.close();
		
		ICompilationUnit to = pack2.getCompilationUnit("Class1.java");

		makeAST(s);//, to);
	}
	
	
	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}