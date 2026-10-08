package br.edu.ifpe.avancajovem

import br.edu.ifpe.avancajovem.model.Estudante
import br.edu.ifpe.avancajovem.model.Meta
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun meta_creation_isCorrect() {
        val meta = Meta(id = 1L, titulo = "Estudar Matemática", descricao = "Equações", pontos = 20, concluida = false)
        assertEquals("Estudar Matemática", meta.titulo)
        assertEquals(20, meta.pontos)
        assertFalse(meta.concluida)
    }

    @Test
    fun estudante_pontos_update_isCorrect() {
        val estudante = Estudante(id = 1L, nome = "Estudante", pontos = 50)
        val novosPontos = estudante.pontos + 20
        val estudanteAtualizado = estudante.copy(pontos = novosPontos)
        assertEquals(70, estudanteAtualizado.pontos)
    }
}
