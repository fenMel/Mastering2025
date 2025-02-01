package fr.esic.mastering.services;

import java.io.File;

import org.springframework.stereotype.Service;

import fr.esic.mastering.entities.Convocation;


@Service
public class PdfService {
	public File genererConvocationPdf(Convocation convocation) {
        
        return new File("convocation.pdf");
}
}