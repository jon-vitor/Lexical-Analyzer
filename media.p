begin
	mediaDasMedias := 0;
	writeln("****Entrada de dados****");
	writeln("digite o total de alunos");
	read(total);
	for cont=1 to total do
	begin
		writeln("Digite os valores da primeira nota do aluno ", cont);
		read(Nota1);
		writeln("Digite os valores da segunda nota do aluno ", cont);
		read(Nota2);
		med := (Nota1+Nota2)/2.0;
		mediaDasMedias := mediasDasMedias+med;
		write("Media = ", med);
	end;
	write("Media Geral =",MediaDasMedias/total);
end.
